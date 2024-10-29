using DataAccessTier_dotNetCore.Models;
using System.Net.Http.Json;


namespace DataAccessTier_dotNetCore.Services
{
    public class ApplicationApiService
    {
        private readonly HttpClient _httpClient;

        public ApplicationApiService(HttpClient httpClient)
        {
            _httpClient = httpClient;
        }

        public async Task<Product?> FetchProductFromApplicationTier(string productId)
        {
            return await _httpClient.GetFromJsonAsync<Product>($"http://localhost:5000/api/products/{productId}");
        }
    }
}
