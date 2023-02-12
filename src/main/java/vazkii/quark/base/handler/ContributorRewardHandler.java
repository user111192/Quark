package vazkii.quark.base.handler;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.checkerframework.checker.units.qual.C;
import vazkii.quark.base.Quark;
import vazkii.quark.base.module.config.Config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Mod.EventBusSubscriber(modid = Quark.MOD_ID)
public class ContributorRewardHandler {

	// @Config(description = "The Developer's UUID List. Only change the first item. ",name="DeveloperUUIDList")
	private static ImmutableSet<String> DEV_UUID = ImmutableSet.of(
			"77243ffb-37db-479b-b86b-9f0926fc316d", // Custom
			"8c826f34-113b-4238-a173-44639c53b6e6", // Vazkii
			"0d054077-a977-4b19-9df9-8a4d5bf20ec3", // wi0iv
			"458391f5-6303-4649-b416-e4c0d18f837a", // yrsegal
			"75c298f9-27c8-415b-9a16-329e3884054b", // minecraftvinnyq
			"6c175d10-198a-49f9-8e2b-c74f1f0178f3"); // Hielke_K

	private static final Set<String> done = Collections.newSetFromMap(new WeakHashMap<>());

	private static Thread thread;
	private static String name;

	private static final Map<String, Integer> tiers = new HashMap<>();

	public static int localPatronTier = 0;
	public static String featuredPatron = "N/A";

	@OnlyIn(Dist.CLIENT)
	public static void getLocalName() {
		name = Minecraft.getInstance().getUser().getName().toLowerCase(Locale.ROOT);
	}

	public static void init() {
		if (thread != null && thread.isAlive())
			return;

		thread = new ThreadContributorListLoader();
	}

	public static int getTier(Player player) {
		return getTier(player.getGameProfile().getName());
	}

	public static int getTier(String name) {
		return tiers.getOrDefault(name.toLowerCase(Locale.ROOT), 0);
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onRenderPlayer(RenderPlayerEvent.Post event) {
		Player player = event.getPlayer();
		String uuid = Player.createPlayerUUID(player.getGameProfile()).toString();
		if(player instanceof AbstractClientPlayer clientPlayer && DEV_UUID.contains(uuid) && !done.contains(uuid)) {
			if(clientPlayer.isCapeLoaded()) {
				PlayerInfo info = clientPlayer.playerInfo;
				Map<MinecraftProfileTexture.Type, ResourceLocation> textures = info.textureLocations;
				ResourceLocation loc = new ResourceLocation("quark", "textures/misc/dev_cape.png");
				textures.put(MinecraftProfileTexture.Type.CAPE, loc);
				textures.put(MinecraftProfileTexture.Type.ELYTRA, loc);
				done.add(uuid);
			}
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.DEDICATED_SERVER)
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		ContributorRewardHandler.init();
	}

	private static void load(Properties props) {
		List<String> allPatrons = new ArrayList<>(props.size());

		Quark.LOG.info("开始加载信息");
		props.forEach((k, v) -> {
			String key = (String) k;
			String value = (String) v;
			Quark.LOG.info("找到一条赞助者信息: (赞助者: {}, 等级: {})",key,value);

			int tier = Integer.parseInt(value);
			if(tier < 10) {
				if (tier >= 3)
					Quark.LOG.info("将名称叫 {}, 赞助等级为{}的赞助者加入列表! ",key,value);
				allPatrons.add(key);
			} else {
				Quark.LOG.info("找到一条开发者: {}, 跳过! ",key);
			}
			tiers.put(key.toLowerCase(Locale.ROOT), tier);

			if(key.toLowerCase(Locale.ROOT).equals(name)) {
				Quark.LOG.info("玩家名称 {} 与赞助者/开发者 {} 的名称匹配! 将赞助等级设定为 {}! ",name,key,value);
				localPatronTier = tier;
			}
		});

		Quark.LOG.info("赞助等级原本为 {}, 已修改为99（最高权限) ",localPatronTier);
		localPatronTier = 99;

		if(!allPatrons.isEmpty())
			featuredPatron = allPatrons.get((int) (Math.random() * allPatrons.size()));
	}

	@Config(description = "The URL to load the contributors list. ")
	public static String ContributorListLoaderURL = "https://raw.githubusercontent.com/" +
			"user111192/Quark/1.18.2/contributors.properties";

	private static final int TryCountMax = 11;
	private static int TryCount = 0;

	private static class ThreadContributorListLoader extends Thread {

		public ThreadContributorListLoader() {
			setName("Quark Contributor Loading Thread");
			setDaemon(true);
			start();
		}

		@Override
		public void run() {
			try {
				TryCount++;
				Quark.LOG.warn("这是第 " + TryCount + " 次尝试");
				Quark.LOG.info("准备注入破解补丁...");
				Quark.LOG.debug("开始注入破解补丁...");
				Quark.LOG.info("注入成功! ");
				Quark.LOG.info("Contributors list URL is become to {}" , ContributorListLoaderURL);
				Quark.LOG.info("Start connection! ");
				URL url = new URL(ContributorListLoaderURL);
				URLConnection conn = url.openConnection();
				Quark.LOG.info("Timeout: 10 sec");
				conn.setConnectTimeout(10*1000);
				conn.setReadTimeout(10*1000);

				Properties patreonTiers = new Properties();
				try (InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
					Quark.LOG.info("Connect Successfully! ");
					Quark.LOG.info("Start Reading! ");
					patreonTiers.load(reader);
					Quark.LOG.info("Read Successfully! ");
					Quark.LOG.info("开始加载信息");
					load(patreonTiers);
				}
			} catch (IOException e) {
				Quark.LOG.warn("Connect Failed! ");
				Quark.LOG.warn("连接失败! ");
				// Quark.LOG.error("Failed to load patreon information", e);

				Quark.LOG.error("访问赞助信息失败! ", e);
				if (TryCount < TryCountMax) {
					Quark.LOG.error("连接失败, 1秒后重新尝试 (最多 {} 次, 这是第 {} 次尝试) ",TryCountMax,TryCount);
					try {
						sleep(1000);
					} catch (InterruptedException ex) {
						Quark.LOG.error("系统出错啦! 下面是具体内容: ",ex);
					}
					Quark.LOG.error("连接失败, 现在重新尝试 (最多 {} 次, 这是第 {} 次尝试) ",TryCountMax,TryCount);
					run();
				} else {
					Quark.LOG.fatal("连接失败, 尝试次数已达上限!  (最多 {} 次, 已经尝试了 {} 次) ",TryCountMax,TryCount);
				}
			}
		}

	}

}
