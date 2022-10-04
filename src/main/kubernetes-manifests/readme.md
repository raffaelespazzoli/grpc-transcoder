```shell
oc delete project test-grpc
oc new-project test-grpc
oc apply -f ./src/main/kubernetes-manifests/ServiceMeshMember.yaml -n test-grpc
oc apply -f ./src/main/kubernetes-manifests/Service.yaml -n test-grpc
oc apply -f ./src/main/kubernetes-manifests/VirtualService.yaml -n test-grpc
oc apply -f ./src/main/kubernetes-manifests/RequestAuthentication.yaml -n test-grpc
oc apply -f ./src/main/kubernetes-manifests/AuthorizationPolicy.yaml -n test-grpc
oc apply -f ./src/main/kubernetes-manifests/EnvoyFilter.yaml -n test-grpc
oc delete secret proto -n test-grpc
oc create secret generic proto --from-file=proto.db=./src/main/envoy/hello-proto.pb -n test-grpc
oc apply -f ./src/main/kubernetes-manifests/Deployment.yaml -n test-grpc
```