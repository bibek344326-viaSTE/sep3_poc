using System.ComponentModel.DataAnnotations;

namespace DataAccessTier_dotNetCore.Models
{
    public class Product
    {
        [Key]
        public required string ProductId { get; set; }
        public required string ProductName { get; set; }
        public double Price { get; set; }
    }
}
