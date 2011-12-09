#!/bin/bash

# This script runs the TypeChef Linux test case with either SuperC or
# TypeChef.  It produces total and per-file timing data.


# Get options.

# Defaults
system="TypeChef"
args= #empty
raw_mode= #empty
unconstrained= #empty

while getopts :a:t:ru opt; do
    case $opt in
        a)
            args=$OPTARG
            ;;
        r)
            raw_mode=true
            ;;
        u)
            unconstrained=true
            ;;
        t)
            system=$OPTARG
            ;;
        \?)
            echo "Invalid argument: -$OPTARG"
            exit;
            ;;
        :)
            echo "-$OPTARG requires an argument."
            exit;
            ;;
    esac
done

shift $(($OPTIND - 1))


# Check that a filename was passed.

file=$1


# Check that a filename was passed.

if [ -z $file ]; then
    echo "USAGE: `basename $0` [-t SuperC|TypeChef] [-a args] file"
    echo ""
    echo "  Run SuperC or TypeChef on the TypeChef.  By default it uses the"
    echo "  partially-configured Linux settings."
    echo ""
    echo "FLAGS:"
    echo "  -t tool  Set the tool to be TypeChef (the default) or SuperC"
    echo "  -a args  Pass extra arguments when running the tool"
    echo "  -u       Don't constrain linux."
    echo "  -r       Raw mode: no extra flags for linux, e.g. -I paths."
    exit 0
fi

if [ ! -f $file ]; then
    echo "Invalid file \"$file\"."
    exit 0
fi

if [[ "$system" != "TypeChef" && "$system" != "SuperC" ]]; then
    echo "Please specify either \"-t TypeChef\" or \"-t Superc\"."
    exit 0
fi


# Make sure the SuperC environment is set up.

if [ -z "$JAVA_DEV_ROOT" ]
then
    "please run env.sh first"
fi


# Make sure the linux source is there.

if [ ! -d "$TYPECHEF_LINUXVER" ]
then
    echo "Please run ./prepareLinuxTestCase.sh from the TypeChef directory " \
        "first."
    exit 1
fi


# Set up TypeChef partial configuration.

srcPath=$PWD/linux-2.6.33.3


flags() {
  name="$1"
  base="$(basename "$1")"
  if grep -q "arch/x86/boot" <<< "$name"; then
    extraFlag="-D _SETUP"
  elif grep -q "arch/x86/kvm" <<< "$name"; then
    extraFlag="-I $srcPath/virt/kvm -I $srcPath/arch/x86/kvm -I $srcPath"
  elif [ "$name" = "net/mac80211/driver-trace" ]; then
    extraFlag="-I $srcPath/net/mac80211"
  elif grep -q "fs/gfs2/" <<< "$name"; then
    extraFlag="-I $srcPath/fs/gfs2"
  elif grep -q "fs/ocfs2/" <<< "$name"; then
    extraFlag="-I $srcPath/fs/ocfs2 -D CATCH_BH_JBD_RACES"
  elif grep -q "fs/xfs/" <<< "$name"; then
    extraFlag="-I $srcPath/fs/xfs -I $srcPath/fs/xfs/linux-2.6"
  elif grep -q "fs/ntfs/" <<< "$name"; then
    extraFlag="-D NTFS_VERSION=\"\\\"2.1.29\"\\\" --include ntfs.h"
  elif grep -q "drivers/gpu/drm/" <<< "$name"; then
    extraFlag="-I $srcPath/include/drm"
  elif egrep -q "drivers/scsi/pcmcia/|drivers/usb/storage/" <<< "$name"; then
    extraFlag="-I $srcPath/drivers/scsi"
  elif grep -q "drivers/scsi/cxgb3i/" <<< "$name"; then
    extraFlag="-I $srcPath/drivers/net/cxgb3"
  elif grep -q "drivers/infiniband/hw/cxgb3/" <<< "$name"; then
    extraFlag="-I $srcPath/drivers/net/cxgb3"
  elif grep -q "drivers/net/skfp/" <<< "$name"; then
    extraFlag="-I $srcPath/drivers/net/skfp -D PCI -D MEM_MAPPED_IO"
  elif grep -q "drivers/staging/rt2860/" <<< "$name"; then
    extraFlag="-D LINUX -D AGGREGATION_SUPPORT -D PIGGYBACK_SUPPORT -D WMM_SUPPORT -D RTMP_MAC_PCI -D RTMP_PCI_SUPPORT -D RT2860 -D RTMP_RF_RW_SUPPORT -D RTMP_EFUSE_SUPPORT -D RT30xx -D RT3090 -D DBG"
  elif grep -q "drivers/staging/rt2870/" <<< "$name"; then
    extraFlag="-D LINUX -D AGGREGATION_SUPPORT -D PIGGYBACK_SUPPORT -D WMM_SUPPORT -D RTMP_MAC_USB -D RTMP_USB_SUPPORT -D RT2870 -D RTMP_TIMER_TASK_SUPPORT -D RTMP_RF_RW_SUPPORT -D RTMP_EFUSE_SUPPORT -D RT30xx -D RT3070 -D DBG"
  elif grep -q "drivers/staging/rtl8192e/" <<< "$name"; then
    extraFlag="-D RTL8192E -D THOMAS_TURBO -D ENABLE_DOT11D"
  elif [ "$name" = "drivers/net/wireless/iwlwifi/iwl-devtrace" ]; then
    extraFlag="-I $srcPath/drivers/net/wireless/iwlwifi/"
  elif grep -q "drivers/scsi/bfa/" <<< "$name"; then
    extraFlag=""
    for path in drivers/scsi/bfa drivers/scsi/bfa/include drivers/scsi/bfa/include/cna; do
      extraFlag="$extraFlag -I $srcPath/$path"
    done
  elif egrep -q "drivers/media/common/tuners/|drivers/staging/go7007/" <<< "$name"; then
    extraFlag=""
    for path in drivers/media/dvb/dvb-core drivers/media/dvb/frontends; do
      extraFlag="$extraFlag -I $srcPath/$path"
    done
    if grep -q "drivers/staging/go7007/" <<< "$name"; then
      extraFlag="$extraFlag -I $srcPath/drivers/media/dvb/dvb-usb"
    fi
  elif egrep -q "drivers/media/video/gspca/(gl860|m5602|stv06xx)/" <<< "$name"; then
    extraFlag="-I $srcPath/drivers/media/video/gspca"
  elif egrep -q "drivers/media/(dvb|video)/|drivers/staging/cx25821/" <<< "$name"; then
    extraFlag=""
    for path in drivers/media/dvb/dvb-core drivers/media/dvb/frontends drivers/media/common/tuners \
      drivers/ieee1394 drivers/media/video/bt8xx drivers/media/video; do
      extraFlag="$extraFlag -I $srcPath/$path"
    done
  else
    extraFlag=""
  fi
  # XXX: again, I need to specify $PWD, for the same bug as above.
  # "-I linux-2.6.33.3/include -I linux-2.6.33.3/arch/x86/include"

  if [[ ! -z $unconstrained ]]; then
      echo "-I $srcPath/include -I $srcPath/arch/x86/include -D KBUILD_BASENAME=\"\\\"$base\\\"\" -D KBUILD_MODNAME=\"\\\"$base\\\"\""
  else
      echo "$extraFlag -I $srcPath/include -I $srcPath/arch/x86/include -D __KERNEL__ -D CONFIG_AS_CFI=1 -D CONFIG_AS_CFI_SIGNAL_FRAME=1 -D KBUILD_BASENAME=\"\\\"$base\\\"\" -D KBUILD_MODNAME=\"\\\"$base\\\"\""
  fi
}

extraFlags="$(flags "$i")"




partialPreprocFlagsTypeChef="-c redhat.properties -x CONFIG_ \
-U __INTEL_COMPILER -U __ASSEMBLY__ --include completedConf.h \
--include partialConf.h --openFeat openFeaturesList.txt -U CONFIG_SPARC \
$extraFlags"


partialPreprocFlagsSuperC="\
-TypeChef-x CONFIG_
-isystem systems/redhat/usr/lib/gcc/x86_64-redhat-linux/4.4.4/include \
-U __INTEL_COMPILER -U __ASSEMBLY__ -include completedConf.h \
-include partialConf.h -U CONFIG_SPARC -TypeChef-x CONFIG_
$extraFlags"

if [[ ! -z $raw_mode ]]; then

    # Don't provide any linux flags.

    partialPreprocFlagsTypeChef=
    partialPreprocFlagsSuperC=


elif [[ ! -z $unconstrained ]]; then

    # Keep linux includes but not the constraints.

    partialPreprocFlagsTypeChef="-c redhat.properties $extraFlags"

    partialPreprocFlagsSuperC="\
-isystem systems/redhat/usr/lib/gcc/x86_64-redhat-linux/4.4.4/include \
$extraFlags"
fi

javaOpts="-Xmx2G -Xms128m -Xss10m"

echo "Processing $file with $system"

if [ "$system" == "SuperC" ]; then
    # Run SuperC on the file.

    echo "# Flags: $partialPreprocFlagsSuperC" >&2

    java $javaOpts xtc.lang.cpp.SuperC -silent \
        $partialPreprocFlagsSuperC $args $file

else
    # Run TypeChef on the file.

    echo "# Flags: $partialPreprocFlagsTypeChef" >&2

    basePath=.
    mainClass="de.fosd.typechef.linux.LinuxPreprocessorFrontend"


    java $javaOpts -cp \
        $basePath/project/boot/scala-2.9.0/lib/scala-library.jar:\
$basePath/PartialPreprocessor/lib/gnu.getopt.jar:\
$basePath/PartialPreprocessor/lib/junit.jar:\
$basePath/lib/junit-4.8.1.jar:\
$basePath/lib/org.sat4j.core-2.3.0.jar:\
$basePath/lib/scalacheck_2.8.1-1.8.jar:\
$basePath/lib/scalatest-1.2.jar:\
$basePath/FeatureExprLib/target/scala_2.9.0/classes:\
$basePath/ConditionalLib/target/scala_2.9.0/classes:\
$basePath/PartialPreprocessor/target/scala_2.9.0/classes:\
$basePath/ParserFramework/target/scala_2.9.0/classes:\
$basePath/CParser/target/scala_2.9.0/classes:\
$basePath/CTypeChecker/target/scala_2.9.0/classes:\
$basePath/LinuxAnalysis/target/scala_2.9.0/classes:\
$basePath/PreprocessorFrontend/target/scala_2.9.0/classes:\
$basePath/ParserFramework/lib_managed/scala_2.9.0/compile/\
kiama_2.9.0-1.1.0.jar \
        $mainClass $partialPreprocFlagsTypeChef $args $file
fi








