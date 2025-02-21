#!/bin/bash

if [ "$#" -lt 1 ]; then
    echo "Usage: $0 <create-data|write-data|read-data> [api_endpoint]"
    exit 1
fi

action="$1"
api_endpoint="${2:-http://localhost:8080}"  # Use http://localhost:8080 as default if not provided
records=10
atomicity="false"  # Use false as default for writes
num_runs=100  # Read 100 times
sleep_time=1  # Use 1 second as default for reads


case "$action" in
    create-data)
        # Make API call to create data
        curl -X GET "${api_endpoint}/api/generateRandomData?records=${records}" --cookie-jar session-cookie.txt
        ;;
    write-data)
        curl -X GET "${api_endpoint}/api/writeData?atomicity=${atomicity}" --cookie session-cookie.txt &
        ;;
    read-data)
        # Make API call to read data specified number of times with sleep time
        for ((i=1; i<=$num_runs; i++)); do
            curl -X GET "${api_endpoint}/api/readData" --cookie session-cookie.txt &
            sleep "$sleep_time"
        done
        wait  # Wait for all background processes to finish
        ;;
    *)
        echo "Invalid option. Use: create-data, write-data, read-data"
        exit 1
        ;;
esac
