using Grpc.Core;
using Grpc.Net.Client;
using Microsoft.Extensions.Logging;
using PresentationTier.Model;
using PresentationTier.Protos;

namespace PresentationTier.Services
{
    public class GrpcProductService
    {
        private readonly ProductService.ProductServiceClient _client;
        private readonly ILogger<GrpcProductService> _logger;

        public GrpcProductService(ProductService.ProductServiceClient client, ILogger<GrpcProductService> logger)
        {
            _client = client;
            _logger = logger ?? throw new ArgumentNullException(nameof(logger)); // Ensure logger is not null
        }


        public async Task<string> AddProductAsync(Product product)
        {
            var request = new ProductRequest
            {
                ProductId = !string.IsNullOrEmpty(product.ProductId) ? product.ProductId : "",
                ProductName = product.ProductName,
                Price = (double)product.Price
            };

            _logger.LogInformation("Adding product: {ProductName}", product.ProductName);

            try
            {
                var response = await _client.AddProductAsync(request);
                _logger.LogInformation("Product added: {ResponseMessage}", response.Message);
                return response.Message;
            }
            catch (RpcException ex)
            {
                _logger.LogError(ex, "gRPC call failed: {StatusCode} - {Message}", ex.StatusCode, ex.Message);
                return "Error adding product.";
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "An unexpected error occurred: {Message}", ex.Message);
                return "Error adding product.";
            }
        }
    }
}
