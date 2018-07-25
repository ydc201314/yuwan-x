package com.yuwan.manager.controller;

import com.yuwan.manager.pojo.Item;
import com.yuwan.manager.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("item/interfaec")
public class ItemInterfaceController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "id", method = RequestMethod.GET)
    public ResponseEntity<Item> queryItemById(@PathVariable("id") Long id) {
        try {
            // 根据ID查询商品信息
            Item item = this.itemService.queryById(id);
            // 设置状态码为200
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果服务器内出现错误返回500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 新增商品
     *
     * @param item
     * @return ResponseEntity<Void>
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveItem(Item item) {
        try {
            this.itemService.save(item);
            // 201
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 如果服务器内部错误，返回500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 修改商品信息
     *
     * @param item
     * @return ResponseEntity<Void>
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item) {
        try {
            this.itemService.updateByIdSelective(item);
            // 204
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 如果服务器内部错误，返回500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 根据ID删除商品
     *
     * @param id
     * @return ResponseEntity<Void>
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteItemById(@PathVariable("id") Long id) {
        try {
            this.itemService.deleteById(id);
            // 204
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 如果服务器内部错误，返回500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
