package fr.fms.web;

import fr.fms.dao.ArticleRepository;
import fr.fms.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    ArticleRepository articleRepository;

    // @RequestMapping(value="/index", method=RequestMethod.GET)
    @GetMapping("/index")
    public String index(Model model, @RequestParam(name="page", defaultValue = "0")int page,
                        @RequestParam(name="keyword", defaultValue = "")String kw){ //le model est fourni par spring, je peux l'utiliser comme ci
        //List<Article> articles = articleRepository.findAll(); //Récup tous les articles
        //Page<Article> articles = articleRepository.findAll(PageRequest.of(page,5));
        Page<Article> articles = articleRepository.findByDescriptionContains(kw, PageRequest.of(page,5));
        model.addAttribute("listArticle", articles.getContent());

        model.addAttribute("pages", new int[articles.getTotalPages()]);

        model.addAttribute("currentPage", page);

        model.addAttribute("keyword",kw);

        // return articles.html
        return "articles"; //cette méthode retourne au dispatcherServerlet une vue
    }

    @GetMapping("/delete")
    public String delete(Long id, int page, String keyword){
        articleRepository.deleteById(id);
        return "redirect:/index?page="+page+"&keyword="+keyword;
    }
}