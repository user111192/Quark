from jsongen import *
import re, os

import os

def main():  
	rootdir = 'templates/vanillawoodstuff'
	tuples = []
	add_tuples(tuples, rootdir)
	generate(tuples, ['chest', 'trapped_chest'])

def add_tuples(tuples, dir):
	for file in os.listdir(dir):
		fpath = os.path.join(dir, file)
		if os.path.isdir(fpath):
			add_tuples(tuples, fpath)
		else: 
			add_tuple(tuples, fpath)
 

def add_tuple(tuples, f):
	raw = f.replace('templates/', '')

	tpl_from = raw
	tpl_to = raw.replace('vanillawoodstuff\\', '').replace('modid', '{modid}').replace('NAME', '{name}')
	tuples.append((tpl_from, tpl_to))

def generate(tuples, prints):
	#copy(tuples)

	for obj in prints:
		fulcaps = obj.replace('_', ' ')
		localize((
			lambda name, modid: 'block.{modid}.{name}_{thing}'.format(name = name, modid = modid, thing = obj),
			lambda name, modid: localize_name(name, modid) + ' ' + fullcaps
		))

	import update_tags
	import update_drop_tables


main()

