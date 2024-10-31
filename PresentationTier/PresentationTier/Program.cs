using PresentationTier.Components;
using PresentationTier.Protos;
using PresentationTier.Services;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddGrpcClient<ProductService.ProductServiceClient>(o =>
{
    o.Address = new Uri("http://localhost:9090/"); // Ensure this matches your server URL
});

// Setup logging
builder.Logging.ClearProviders();
builder.Logging.AddConsole(); // To log to the console
builder.Logging.AddDebug();   // Optional: To log to the debug output window

// Add services to the container (only once)
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();
app.UseAntiforgery();

// Map the Razor components
app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();
