import os
import sys

print sys.argv

if (len(sys.argv) != 3):
	print "use like python translate.py [MAIN_CLASS_FILE] [CLASSPATH]"
else:
	result = os.system("make")

	if (result != 0):
		print "Build failed"
	else:
		mainClass = sys.argv[1]
		classPath = sys.argv[2]
		os.system("java xtc.translator.translation.Translator " + mainClass + " " + classPath)
		os.chdir("out")
		os.system("g++ *.cc")
