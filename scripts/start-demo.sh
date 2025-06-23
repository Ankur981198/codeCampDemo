#!/bin/bash

# Clean everything
docker stop $(docker ps -aq) 2>/dev/null || true
docker rm $(docker ps -aq) 2>/dev/null || true

echo ""
echo "Starting API with PORT BINDING (not networking)..."
cd ..
cd mock-api
docker build -t final-api .
docker run -d --name final-api -p 8090:8090 final-api
cd ..

echo ""
echo "Waiting 15 seconds..."
sleep 15

echo ""
echo "🔍 Testing API directly..."
curl http://localhost:8090/api/tasks/health
if [ $? -eq 0 ]; then
    echo "✅ API is working!"
else
    echo "API not working - stopping here"
    docker logs final-api
    exit 1
fi

echo ""
echo "🧪 Running SIMPLE tests..."
cd api-tests

# Check if Maven is available locally
if command -v mvn > /dev/null 2>&1; then
    echo "Using LOCAL Maven"
    mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testng.xml
else
    echo "🐳 Using Docker but with host network access..."
    docker build -t final-tests .
    # Use host network so container can access localhost:8090
    docker run --rm --network host \
        -v $(pwd)/../test-reports:/app/target/surefire-reports \
        final-tests \
        mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng.xml
fi

cd ..

echo ""
echo "🎉 DEMO COMPLETE!"
echo "📊 Check test results:"
find . -name "TEST-*.xml" -path "*/surefire-reports/*" 2>/dev/null

echo ""
echo "🛑 Cleanup: docker stop final-api && docker rm final-api"

