using Grpc.Net.Client;
using PresentationTier.Components;
using Microsoft.Extensions.DependencyInjection;
using PresentationTier.Services;
using PresentationTier.Protos;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();


// Configure gRPC channel for Blazor Server
builder.Services.AddSingleton(services =>
{
    var channel = GrpcChannel.ForAddress("https://localhost:9090");
    return new ProductService.ProductServiceClient(channel);
});

builder.Services.AddGrpcClient<ProductService.ProductServiceClient>(option =>
{
    option.Address = new Uri("http://localhost:9090");
});


var app = builder.Build();


// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}

app.UseHttpsRedirection();

app.UseStaticFiles();
app.UseAntiforgery();

app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();
