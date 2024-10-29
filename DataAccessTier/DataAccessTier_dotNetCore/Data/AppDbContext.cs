using DataAccessTier_dotNetCore.Models;
using Microsoft.EntityFrameworkCore;

namespace DataAccessTier_dotNetCore.Data
{
    public class AppDbContext: DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

        public DbSet<Product> Products { get; set; }
    }
}
