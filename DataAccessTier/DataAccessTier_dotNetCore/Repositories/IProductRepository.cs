using DataAccessTier_dotNetCore.Models;

namespace DataAccessTier_dotNetCore.Repositories
{
    public interface IProductRepository
    {
        Task AddProductAsync(Product product);
        Task<Product> GetProductByIdAsync(string productId);
        Task<IEnumerable<Product>> GetAllProductsAsync();
    }
}
