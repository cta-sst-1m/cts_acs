export ACS_INSTANCE=4
export ACS_CDB=$PWD/test

export JAVA_HOME=/usr/java/jdk1.8.0_45/
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JAVA_HOME/lib/amd64/:$JAVA_HOME/lib/amd64/jli/

PREFERED_ACS_VERSION="ACS-2016.2"

export INTROOT=$HOME/acs/INTROOT
export ACSDATA=$HOME/acs/ACSDATA
export ACS_RETAIN=1

export JAVA_OPTIONS="-Dlog4j.debug=true -Dlog4j.configuration=file:$INTROOT/config/log4j.properties"
export PATH=$HOME/bin:$JAVA_HOME/bin:$PATH

if [[ $- =~ "i" ]]; then
  echo Setting INTROOT to $INTROOT
fi

. /alma/${PREFERED_ACS_VERSION}/ACSSW/config/.acs/.bash_profile.acs

if [[ $- =~ "i" ]]; then
  echo Setting ACSDATA to $ACSDATA
fi

export LD_LIBRARY_PATH=.:/opt/rh/v8314/root/usr/lib64:/opt/rh/nodejs010/root/usr/lib64:$LD_LIBRARY_PATH
export PATH=.:/opt/rh/nodejs010/root/usr/bin:.:/opt/rh/devtoolset-3/root/usr/bin:$HOME/software/bin/:$PATH

#export PYTHONPATH=$PYTHONPATH:$DEV_PATH/drive/MST/drive_system/trunk/:$HOME/software/CCDs/pymba/:$HOME/software/control/master/MasterSST1M/src/sstmaster:$HOME/software/control/other

export PYTHONSTARTUP=~/.pystartup

export BUILD_TYPE=ACS

