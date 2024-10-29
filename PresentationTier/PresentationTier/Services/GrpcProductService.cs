using Grpc.Net.Client;
using PresentationTier.Model;
using PresentationTier.Protos;
using System.Collections;

namespace PresentationTier.Services
{
    public class GrpcProductService
    {
        private readonly ProductService.ProductServiceClient _client;

        public GrpcProductService(GrpcChannel grpcChannel)
        {
            _client = new ProductService.ProductServiceClient(grpcChannel);
        }

        public async Task<String> AddProductAsync(Product product)
        {
            var request = new ProductRequest
            {
                ProductId = !string.IsNullOrEmpty(product.Id) ? product.Id : "",
                ProductName = product.Name,
                Price = (double)product.Price
            };

            var response = await _client.AddProductAsync(request);
            Console.WriteLine("Product added: " + response.Message);
            return response.Message;
        }

        
    }
}
