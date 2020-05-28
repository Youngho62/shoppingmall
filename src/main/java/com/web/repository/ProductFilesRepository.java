package com.web.repository;

import com.web.domain.Product;
import com.web.domain.ProductFiles;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductFilesRepository extends CrudRepository<ProductFiles,Long> {
    @Query("select pf from ProductFiles pf where pf.product=?1 and  pf.fno > 0 order by pf.fno desc")
    List<ProductFiles> getRepliesofProduct(Product product);

    void deleteAllByProduct(Product product);

    @Query("select pf from ProductFiles pf where pf.uploadUrl=?1")
    List<ProductFiles> getOldFiles(String yesterdayFolder);

    ProductFiles findProductFilesByUuid(String uuid);
}
