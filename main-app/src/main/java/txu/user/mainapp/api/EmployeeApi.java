package txu.user.mainapp.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import txu.user.mainapp.base.AbstractApi;
import txu.user.mainapp.dao.EmployeeDao;
import txu.user.mainapp.dto.GetAllEmploeeRequest;
import txu.user.mainapp.entity.EmployeeEntity;

import java.util.List;
//@CrossOrigin(origins = "https://main.d229jj886cbsbs.amplifyapp.com", allowedHeaders = "*")
//@CrossOrigin
//@CrossOrigin(origins = "*")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "*")


@CrossOrigin(origins = "*", allowCredentials = "false", maxAge = 86400, allowedHeaders = "*")
@RestController
//@RequestMapping("/product")
@RequiredArgsConstructor
public class EmployeeApi extends AbstractApi {

    private final EmployeeDao employeeDao;

//    @PostMapping(consumes = "application/json")
//    public ProductEntity createOrUpdate(@RequestBody CreateUpdateProductRequest createUpdateProductRequest){
//        return productService.createOrUpdate(createUpdateProductRequest.getProduct(), createUpdateProductRequest.getCategories());
//    }
//
//    @DeleteMapping(consumes = "application/json")
//    public void delete(@RequestBody ProductRequest productRequest){
//        productService.delete(productRequest.getProductId());
//    }
//
//    @PostMapping(value = "search",  consumes = "application/json")
//    public List<ProductEntity> search(@RequestBody SearchProductRequest searchProductRequest) {
//        if (StringUtils.isNullOrEmpty(searchProductRequest.getKeySearch())){return null;}
//        return filter(searchProductRequest);
//    }


    @PostMapping(value = "get-all",  consumes = "application/json")
    public List<EmployeeEntity> getAll(@RequestBody GetAllEmploeeRequest request) {
        return employeeDao.getAll();
    }
}
