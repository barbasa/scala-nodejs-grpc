.PHONY: node_protoc_install init_env proto

export NODEJSPATH := $(CURDIR)
export NODEJSLIBPATH=${NODEJSPATH}/lib

init_env: node_protoc_install proto
	npm install

node_protoc_install:
	npm install -g grpc-tools

PROTO_SRC_DIR := $(CURDIR)/../api/src/main/protobuf
proto:
	mkdir -p ${NODEJSLIBPATH}/proto
	grpc_tools_node_protoc \
		--js_out=import_style=commonjs,binary:${NODEJSLIBPATH}/proto \
		--grpc_out=${NODEJSLIBPATH}/proto \
		--plugin=protoc-gen-grpc=`which grpc_tools_node_protoc_plugin` \
		--proto_path ${PROTO_SRC_DIR} \
		${PROTO_SRC_DIR}/*.proto
