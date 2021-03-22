import Model.Entity.Product;
import Model.Service.ProductServiceImp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ProductController", urlPatterns = "/productController")
public class ProductController extends HttpServlet {
    ProductServiceImp productServiceImp = new ProductServiceImp();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("ac");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create" -> showAddForm(request, response);
                case "delete" -> deleteProduct(request, response);
                case "update" -> showEditForm(request,response);
                default -> showProduct(request,response);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("ac");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create" -> {
                try {
                    insertProduct(request,response);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            case "update" -> {
                try {
                    updateProduct(request,response);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            case "search" -> {
                try {
                    searchProduct(request,response);
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        }
    }
    protected void searchProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        String productName = request.getParameter("productName");
        List<Product> productList= (List<Product>) productServiceImp.searchProduct(productName);
        request.setAttribute("searching",productList);
        RequestDispatcher dispatcher=request.getRequestDispatcher("searchProduct.jsp");
        dispatcher.forward(request,response);
    }

    protected void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("createProduct.jsp");
        dispatcher.forward(request,response);
    }
    protected void showProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        List<Product> list = productServiceImp.selectAllProduct();
        request.setAttribute("listProduct",list);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request,response);
    }
    protected void insertProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        String productName = request.getParameter("productName");
        double productPrice = Double.parseDouble(request.getParameter("productPrice"));
        int productQuantity = Integer.parseInt(request.getParameter("productQuantity"));
        String productColor = request.getParameter("productColor");
        String categoryName = request.getParameter("categoryName");
        Product product = new Product(productName,productPrice,productQuantity,productColor,categoryName);
        productServiceImp.insertProduct(product);
        response.sendRedirect("/productController");
    }

    protected void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        int idDelete = Integer.parseInt(request.getParameter("idDelete"));
        productServiceImp.deleteProduct(idDelete);
        response.sendRedirect("/productController");
    }

    protected void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        int idUpdate = Integer.parseInt(request.getParameter("idUpdate"));
        Product product = productServiceImp.selectProductById(idUpdate);
        request.setAttribute("product",product);
        RequestDispatcher dispatcher = request.getRequestDispatcher("updateProduct.jsp");
        dispatcher.forward(request,response);
    }

    protected void updateProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        String productName = request.getParameter("productName");
        double productPrice = Double.parseDouble(request.getParameter("productPrice"));
        int productQuantity = Integer.parseInt(request.getParameter("productQuantity"));
        String productColor = request.getParameter("productColor");
        String categoryName = request.getParameter("categoryName");
        Product product = new Product(productName,productPrice,productQuantity,productColor,categoryName);
        productServiceImp.updateProduct(product,productId);
        response.sendRedirect("/productController");
    }
}
