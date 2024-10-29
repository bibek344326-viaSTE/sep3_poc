namespace PresentationTier.Model
{
    public class Product
    {
        public string Id { get; set; }  // Unique identifier for the product
        public string Name { get; set; } // Name of the product
        public decimal Price { get; set; } // Price of the product

        public Product()
        {
            Id = "";
            Name = "";
            Price = 0;
        }
        public Product(string Id, string name, decimal price)
        {
            this.Id = Id;
            Name = name;
            Price = price;
        }
    }
}
