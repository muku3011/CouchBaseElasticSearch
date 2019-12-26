#!/bin/sh

echo 'Launching couchbase'

# Executing script in background
/usr/sbin/runsvdir-start &

configureCouchbase() {
  echo 'Creating cluster'
  couchbase-cli cluster-init -c 127.0.0.1:8091 --cluster-username="${CB_USER}" --cluster-password="${CB_PWD}" --cluster-ramsize=512 --cluster-index-ramsize=512 --cluster-fts-ramsize=256 --services=data,index,query,fts
  echo 'Creating '"${CB_BUCKET}"' bucket'
  couchbase-cli bucket-create -c 127.0.0.1:8091 -u "${CB_USER}" -p "${CB_PWD}" --bucket="${CB_BUCKET}" --bucket-type=couchbase --bucket-ramsize=300
  echo 'Initialization performed'
}

initializeCouchbase() {
  maximum_retry=10
  while [ "$maximum_retry" -ge 0 ]; do
    echo 'Couchbase is starting (rest api not available)'
    response=$(curl --write-out %"{http_code}" --silent --output /dev/null http://127.0.0.1:8091)

    # Couchbase started if HTTP response code is 301
    if [ "$response" -eq 301 ]; then # asdad
      echo 'Configuring Couchbase'
      configureCouchbase
      return
    else
      printf 'Couchbase not started yet, waiting 5 more seconds ...'
      maximum_retry=$((maximum_retry - 1))
      sleep 5
    fi
  done
  # Exit if Couchbase doesn't start in maximum_retry*sleep seconds, i.e., 10×5 = ~50 seconds
  printf 'Couchbase not started in defined time, exiting ...'
  exit 0
}

initializeCouchbase
printf 'Couchbase configured and initialized.'

tail -f /dev/null
