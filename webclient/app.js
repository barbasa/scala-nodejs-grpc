let grpc = require('grpc');
let services = require('./lib/proto/restaurants_grpc_pb');
let messages = require('./lib/proto/restaurants_pb');

const API_URL = '127.0.0.1';
const API_PORT = '50051';

console.log(`Connecting to API ${API_URL}:${API_PORT}`);

let client = new services.SearchRestaurantServiceClient(`${API_URL}:${API_PORT}`, grpc.credentials.createInsecure());

let protoRequest = new messages.SearchRestaurantRequest();
// protoRequest.setPostcode("w120je")

client.searchRestaurant(protoRequest, function(err,response){
    if (err) {
        console.error(err)
    }
    response.getRestaurantsList().map(function (r) {
        console.log(r.toObject())
    });
})
