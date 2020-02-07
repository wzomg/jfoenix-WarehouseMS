package dao;

import org.apache.ibatis.annotations.Param;
import patterns.prototype.Goods;

import java.util.List;

public interface IGoodsDao {

    // 增加一个或多个商品
    int addGoods(List<Goods> goosList);

    //查询所有商品信息
    List<Goods> findAllGoods();

    // 查询某个商品
    Goods findGoodById(Integer gid);

    //根据商品id出库商品，det为减少的数量
    int updateGoodsById(@Param("gid") Integer gid, @Param("det") Integer det);

    //根据商品id删除某个商品，包括减少的数量
    int deleteGoodById(Integer gid);
}
