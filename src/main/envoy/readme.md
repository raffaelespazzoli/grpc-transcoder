# setup

```shell
# there is probably a better way to do this
git clone https://github.com/googleapis/googleapis
GOOGLEAPIS_DIR=./
protoc -I${GOOGLEAPIS_DIR} -I. --include_imports --include_source_info --descriptor_set_out=./src/main/envoy/hello-proto.pb ./src/main/proto/hello.proto
protoc -I./src/main/proto --include_imports --include_source_info --descriptor_set_out=./src/main/envoy/hello-proto.pb ./src/main/proto/hello.proto
```

run envoy

```shell
envoy -c ./src/main/envoy/envoy-demo.yaml --config-yaml "$(cat ./src/main/envoy/envoy-override.yaml)"
```

generating a swagger doc

```shell
go install github.com/google/gnostic/cmd/protoc-gen-openapi@v0.6.9
go install \
    github.com/grpc-ecosystem/grpc-gateway/v2/protoc-gen-grpc-gateway \
    github.com/grpc-ecosystem/grpc-gateway/v2/protoc-gen-openapiv2 \
    google.golang.org/protobuf/cmd/protoc-gen-go \
    google.golang.org/grpc/cmd/protoc-gen-go-grpc
protoc -I./src/main/proto --openapi_out=./src/main/envoy ./src/main/proto/hello.proto
```

```shell
go install \
    github.com/grpc-ecosystem/grpc-gateway/v2/protoc-gen-grpc-gateway@v2.11.3 \
    github.com/grpc-ecosystem/grpc-gateway/v2/protoc-gen-openapiv2@v2.11.3
protoc -I./src/main/proto --openapiv2_out ./src/main/envoy --openapiv2_opt logtostderr=true ./src/main/proto/hello.proto    
```