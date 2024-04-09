package fr.fms.web;

import fr.fms.dao.ArticleRepository;
import fr.fms.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    ArticleRepository articleRepository;

    // @RequestMapping(value="/index", method=RequestMethod.GET)
    @GetMapping("/index")
    public String index(Model model){ //le model est fourni par spring, je peux l'utiliser comme ci
        List<Article> articles = articleRepository.findAll(); //Récup tous les articles
        model.addAttribute("listArticle", articles);
        // return articles.html
        return "articles"; //cette méthode retourne au dispatcherServerlet une vue
    }
}