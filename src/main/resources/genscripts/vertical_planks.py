from jsongen import *

copy([
	('unique/block_model_vertical_planks.json', 'assets/{modid}/models/block/{name}.json'),
	('block_item_generic.json', 'assets/{modid}/models/item/{name}.json'),
	('blockstate_generic.json', 'assets/{modid}/blockstates/{name}.json'),
	('drop_table_generic.json', 'data/{modid}/loot_tables/blocks/{name}.json'),

	('unique/recipe_vertical_planks.json', 'data/{modid}/recipes/building/crafting/vertical_{name}_planks.json'),
	('unique/recipe_vertical_planks_revert.json', 'data/{modid}/recipes/building/crafting/vertical_{name}_planks_revert.json')
])

localize_standard('block')

import update_drop_tables