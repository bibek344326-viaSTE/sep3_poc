using DataAccessTier_dotNetCore.Models;
using DataAccessTier_dotNetCore.Repositories;
using DataAccessTier_dotNetCore.Services;
using Microsoft.AspNetCore.Mvc;

namespace DataAccessTier_dotNetCore.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ProductsController : ControllerBase
    {
        private readonly IProductRepository _productRepository;
        private readonly ApplicationApiService _apiService;

        public ProductsController(IProductRepository productRepository, ApplicationApiService apiService)
        {
            _productRepository = productRepository;
            _apiService = apiService;
        }

        [HttpPost]
        public async Task<IActionResult> AddProduct([FromBody] Product product)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            await _productRepository.AddProductAsync(product);
            return CreatedAtAction(nameof(AddProduct), new { id = product.ProductId }, product);

        }

        [HttpGet("{productId}")]
        public async Task<IActionResult> GetProductById(string productId)
        {
            var product = await _productRepository.GetProductByIdAsync(productId);
            if (product == null)
            {
                return NotFound(new { message = $"Product with ID {productId} not found." });
            }
            return Ok(product);
        }

        [HttpGet]
        public async Task<IActionResult> GetAllProducts()
        {
            var products = await _productRepository.GetAllProductsAsync();
            return Ok(products);
        }
    }
}
