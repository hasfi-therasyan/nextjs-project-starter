# Skincare App

An Android application that demonstrates dual API implementation (REST and GraphQL) for fetching skincare products from a PostgreSQL database.

## Features

- Dual API support:
  - REST API using Retrofit
  - GraphQL using Apollo Client
- Modern UI with Material Design
- MVVM Architecture
- Kotlin Coroutines for async operations
- Image loading with Glide
- Dark mode support

## Tech Stack

- Kotlin
- Android Architecture Components
  - ViewModel
  - LiveData
  - Coroutines
- Retrofit for REST API
- Apollo for GraphQL
- Glide for image loading
- Material Design Components

## Project Structure

```
app/
├── src/
│   └── main/
│       ├── java/com/skincare/apitest/
│       │   ├── model/
│       │   │   └── Product.kt
│       │   ├── network/
│       │   │   └── ProductService.kt
│       │   ├── repository/
│       │   │   └── ProductRepository.kt
│       │   ├── ui/
│       │   │   └── ProductAdapter.kt
│       │   ├── viewmodel/
│       │   │   └── ProductViewModel.kt
│       │   └── MainActivity.kt
│       └── graphql/
│           └── com/skincare/apitest/
│               └── GetProducts.graphql
```

## Setup

1. Clone the repository:
```bash
git clone https://github.com/yourusername/skincare-app.git
```

2. Open the project in Android Studio

3. Update the API endpoints:
   - REST API: Update `BASE_URL` in `ProductService.kt`
   - GraphQL: Update `SERVER_URL` in `ProductService.kt`

4. Build and run the app

## Database Schema

The app expects a PostgreSQL database with the following table structure:

```sql
CREATE TABLE individual_products (
    id SERIAL PRIMARY KEY,
    product_name TEXT,
    description TEXT,
    price NUMERIC,
    image_data BYTEA
);
```

## Usage

1. Launch the app
2. Select the API type (REST or GraphQL) from the dropdown
3. Click "Fetch Products" to load the product list
4. View product details including images, descriptions, and prices

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
