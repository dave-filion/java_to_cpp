import os
import sys

if (len(sys.argv) != 3):
        print "use like python translate.py [MAIN_CLASS_FILE] [CLASSPATH]"
else:
        os.system("rm out/*")
        result = os.system("make -s")

        if (result != 0):
                print "Build failed"
        else:
                mainClass = sys.argv[1]
                classPath = sys.argv[2]
                os.system("java xtc.translator.translation.Translator " + mainClass + " " + classPath)
                os.chdir("out")
                result2 = os.system("g++ *.cc")

                if (result2 != 0):
                        print "Compile Failed"
                else:
                        print "Sucess!"
