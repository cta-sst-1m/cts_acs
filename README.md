# Alma Control System for Camera Test setup
An implementation of the Alma Control System (ACS) for the Camera Test setup (CTS).

# Instalation
## requirements
ACS must be installed (that is the case on SST1M_server and PDP_server).

## clone the repository
run the following command where you want an acs_cts folder to be created:
    git clone https://github.com/cta-sst-1m/cts_acs.git

## compilation    
run the activate_ACS bash function:
    1. Start the environment with:
        source ./activate_ACS.sh
    
    2. go into the src/ folder with:
        cd src

    3. compile with:
        make

    4. optional: install globally with:
        make install

## connect to the server
check with `acscommandcenter` that all looks ok.
