#!/bin/sh

TIMEOUT=60
SLEEPTIME=5
INITIAL_WAIT=10

# ------------------------------------------------------------------------

usage() {
	echo "Usage: $0 [-t TIMEOUT] [-s SLEEPTIME] [-i INITIAL_WAIT]"
	echo "Waits until a given list of urls and host:port combinations can be accessed."
	echo "The urls and host:port combinations are read from environment variables."
	echo "At least one url or one host:port has to be set."
	echo " "
	echo "Parameters:"
	echo "  -t              The maximal time in seconds that is waited for one url or "
	echo "                  host:port. This is not exact, the script may wait longer."
	echo "                  The default value for TIMEOUT: 120."
	echo "  -s              The time in seconds that this script is sleeping between "
	echo "                  attempts to access the url/host:port. The default value "
	echo "                  for SLEEPTIME: 2."
	echo "  -i              The time in seconds that this script will wait until it "
	echo "                  starts testing the port or url availability."
	echo "                  The default value for INITIAL_WAIT: 10."
	echo "Environment variables:"
	echo "  WAIT_FOR_URLS   List of urls to check, the urls are separated by ','."
	echo "  WAIT_FOR_PORTS  List of host:port-combinations to check."
	echo "                  Each host:port combination has the format 'host:port'."
	echo "                  The host:port combinations are separated by ','."
	echo "Exit codes:"
	echo "  0               All urls and ports are accessible"
	echo "  1               A url or post is not accessible"
	echo "  2               A configuration error or parameter --help was found."
	echo "                  No waiting/checking of urls or ports has been done."
	exit 2
}

# ---------- Checks the availability of one url

# $1: url
wait_for_url() {

	URL=$1

	echo "Checking if ${URL} is available..."

	SERVICE_UP=0
	TIME_CONSUMED=0

	until [ $TIME_CONSUMED -gt $TIMEOUT -o $SERVICE_UP -gt 0 ]
	do
		wget -q -P /tmp $URL > /dev/null 2>&1
		if [ $? -eq 0 ]
		then
			SERVICE_UP=1
		else
			echo "Waiting for availability of ${URL}..."
			sleep ${SLEEPTIME}s
			TIME_CONSUMED=`expr $TIME_CONSUMED + $SLEEPTIME`
		fi
	done

	if [ $SERVICE_UP -eq 1 ]
	then
	    echo "Availability of ${URL} confirmed."
	else
	    echo "Timeout at waiting on availability of ${URL}, service appears to be down!"
		exit 1
	fi
}

# ---------- Checks the availability of one port on a given host

# $1: host:port
wait_for_port() {

	HOST=$(printf "%s\n" "$1"| cut -d : -f 1)
  PORT=$(printf "%s\n" "$1"| cut -d : -f 2)

	echo "Checking if ${HOST}:${PORT} is available..."

	SERVICE_UP=0
	TIME_CONSUMED=0

	until [ $TIME_CONSUMED -gt $TIMEOUT -o $SERVICE_UP -gt 0 ]
	do
		nc -z "$HOST" "$PORT"
		if [ $? -eq 0 ]
		then
			SERVICE_UP=1
		else
			echo "Waiting for availability of ${HOST}:${PORT}..."
			sleep ${SLEEPTIME}s
			TIME_CONSUMED=`expr $TIME_CONSUMED + $SLEEPTIME`
		fi
	done

	if [ $SERVICE_UP -eq 1 ]
	then
	    echo "Availability of ${HOST}:${PORT} confirmed."
	else
	    echo "Timeout at waiting on availability of ${HOST}:${PORT}, service appears to be down!"
		exit 1
	fi
}

# ------------------------------------------------------------------------

# ---------- Parse command line parameters

while [ $# -gt 0 ]
do
  case "$1" in
    -t)
    TIMEOUT=$2
    shift 2
    ;;
    -s)
    SLEEPTIME=$2
    shift 2
    ;;
    -i)
    INITIAL_WAIT=$2
    shift 2
    ;;
    --help)
    usage
    ;;
    *)
    echoerr "Unknown argument: $1"
    usage
    ;;
  esac
done

echo "Initial wait:        ${INITIAL_WAIT} seconds"
echo "Timeout:             ${TIMEOUT} seconds"
echo "Sleep between tries: ${SLEEPTIME} seconds"

if [ -z "$WAIT_FOR_URLS" ]
  then
	if [ -z "$WAIT_FOR_PORTS" ]
	then
		echo "No urls or host:port definitions to check!"
		usage
	fi
fi

# ---------- A little initial wait

echo "Initially waiting ${INITIAL_WAIT} seconds..."
sleep ${INITIAL_WAIT}s

# ---------- Check host:ports

set -f; IFS=,
for HOSTPORT in $WAIT_FOR_PORTS
do
  wait_for_port $HOSTPORT
done
set +f; unset IFS

# ---------- Check urls

set -f; IFS=,
for URL in $WAIT_FOR_URLS
do
  wait_for_url $URL
done
set +f; unset IFS