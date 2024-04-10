package fr.fms.web;

import fr.fms.dao.ArticleRepository;
import fr.fms.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class ArticleController {
    @Autowired
    ArticleRepository articleRepository;

    public static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }

    // @RequestMapping(value="/index", method=RequestMethod.GET)
    @GetMapping("/index")
    public String index(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "keyword", defaultValue = "") String kw) { //le model est fourni par spring, je peux l'utiliser comme ci
        //List<Article> articles = articleRepository.findAll(); //Récup tous les articles
        //Page<Article> articles = articleRepository.findAll(PageRequest.of(page,5));
        Page<Article> articles = articleRepository.findByDescriptionContains(kw, PageRequest.of(page, 5));
        model.addAttribute("listArticle", articles.getContent());

        model.addAttribute("pages", new int[articles.getTotalPages()]);

        model.addAttribute("currentPage", page);

        model.addAttribute("keyword", kw);

        // return articles.html
        return "articles"; //cette méthode retourne au dispatcherServerlet une vue
    }

    // delete button methode
    @GetMapping("/delete")
    public String delete(Long id, int page, String keyword) {
        articleRepository.deleteById(id);
        return "redirect:/index?page=" + page + "&keyword=" + keyword;
    }

    // add new article
    @GetMapping("/article")
    public String article(Model model) {
        // on inject un article par defaut dans le formulaire de saisi
        model.addAttribute("article", new Article());
        return "article";
    }

    // update button methode
    @GetMapping("/update")
    public String update(Model model, Long id) {
        // on inject un article par defaut dans le formulaire de saisi
        Optional<Article> articleToUpdate = articleRepository.findById(id);
        Article article = articleToUpdate.orElse(null);
        model.addAttribute("article", article);
        return "update";
    }

    //New article save methode
    @PostMapping("/save")
    public String save(@Valid Article article, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "article";
        // s'il n'y a pas de saisie d'un champ selon certains critères, pas d'insertion en base
        articleRepository.save(article);
        return "redirect:/index";
    }

    //article update methode
    @PostMapping("/toUpdate")
    public String toUpdate(Long id, @Valid Article updatedArticle, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "article";
        Optional<Article> articleToUpdate = articleRepository.findById(id);
        Article article = articleToUpdate.get();
        article.setId(updatedArticle.getId());
        article.setDescription(updatedArticle.getDescription());
        article.setPrice(updatedArticle.getPrice());
        articleRepository.save(article);
        return "redirect:/index";
    }
}