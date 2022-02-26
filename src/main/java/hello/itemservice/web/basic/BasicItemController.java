package hello.itemservice.web.basic;


import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
    private final ItemRepository itemRepository;

    /*
    @Autowired
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    ** @RequiredArgsConstructor이 이 역할
     */

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                              @RequestParam int price,
                              @RequestParam Integer quantity,
                              Model model){
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);
        return "basic/item";
    }

    /**
     * @ModelAttribute 는 두가지 역할을 한다
     * 1. 요청 파라미터로 온 데이터들을 알아서 Item 객체에 넣어줌
     * 2. 모델에 자동으로 추가해준다 @ModelAttribute("modelName") Item item -> model.addAttribute("modelName", item);
     */
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item,
                            Model model){

        itemRepository.save(item);

//        model.addAttribute("item", item);
        return "basic/item";
    }

    /**
     * @ModelAttribute ClassName classname -> @ModelAttribute("classname") ClassName classname -> model.addAttribute("classname", classname);
     */
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item,
                            Model model){

        itemRepository.save(item);

//        model.addAttribute("item", item);
        return "basic/item";
    }

    /**
     * @ModelAttribute 는 생략가능
     */
//    @PostMapping("/add")
    public String addItemV4(Item item,
                            Model model){

        itemRepository.save(item);

//        model.addAttribute("item", item);
        // 기존에 사용하던 뷰 템플릿으로 보여주는 것이기때문에 url은 /basic/items/add 그대로이다.
        return "basic/item";
    }

    /**
     * @ModelAttribute 는 생략가능
     */
//    @PostMapping("/add")
    public String addItemV5(Item item,
                            Model model){

        itemRepository.save(item);

//        model.addAttribute("item", item);
        //redirect로 해야 새로고침을 해도 post방식이 동작하지 않는다
        return "redirect:/basic/items/" + item.getId();
    }

    /**
     * @ModelAttribute 는 생략가능
     */
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes){

        Item savedItem = itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        //redirectAttributes.addAttribute("itemId", savedItem.getId()); 여기에서의 itemId가 치환된다
        // url 상에 없으면 요청 파라미터로 바뀐다 /basic/items/3?status=True
        return "redirect:/basic/items/{itemId}";

    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }


    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        // 상품 상세로 redirect
        return "redirect:/basic/items/{itemId}";
    }

    @PostConstruct
    public void init() {

        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 10000, 10));
    }


}
