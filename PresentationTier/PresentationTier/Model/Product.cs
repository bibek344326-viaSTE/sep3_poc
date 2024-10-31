namespace PresentationTier.Model
{
    public class Product
    {
        public string ProductId { get; set; }  // Unique identifier for the product
        public string ProductName { get; set; } // Name of the product
        public decimal Price { get; set; } // Price of the product

        public Product()
        {
            ProductId = "";
            ProductName = "";
            Price = 0;
        }
        public Product(string Id, string name, decimal price)
        {
            this.ProductId = Id;
            ProductName = name;
            Price = price;
        }
    }
}
