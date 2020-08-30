#!/bin/sh

echo 'Launching elasticsearch'

# Executing elasticsearch entrypoint in background
/usr/local/bin/docker-entrypoint.sh &

configureElasticsearch() {
  # Base64 conversion for making API call with a username and password
  authorization=$(echo "${ES_USER}":"${ES_PWD}" | base64)
  # File path for Index mapping
  filename=/usr/local/bin/mapping.json
  curl --location --request PUT "localhost:9200/${ES_INDEX}" --header 'Content-Type: application/json' --header "Authorization: Basic ${authorization}" --data-binary "@${filename}"
  printf '\nElasticsearch index created with mapping from mapping.json file.\n'
}

initializeElasticsearch() {
  maximum_retry=10
  while [ "$maximum_retry" -ge 0 ]; do
    # Checking Elasticsearch status
    response=$(curl --write-out %"{http_code}" --silent --output /dev/null http://127.0.0.1:9200)

    # Elasticsearch started if HTTP response code is 200
    if [ "$response" -eq 200 ]; then # asdad
      printf '\nConfiguring Elasticsearch\n'
      # configureElasticsearch
      return
    else
      printf '\nElasticsearch not started yet, waiting 5 more seconds ...\n'
      maximum_retry=$((maximum_retry - 1))
      sleep 5
    fi
  done
  # Exit if Elastic search doesn't start in maximum_retry*sleep seconds, i.e., 10×5 = ~50 seconds
  printf '\nElasticsearch not started in defined time, exiting ...\n'
  exit 0
}

initializeElasticsearch
printf '\nElasticsearch configured and initialized.\n'

tail -f /dev/null
