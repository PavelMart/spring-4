package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.JavaConfig;
import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;
import ru.netology.repository.PostRepository;
import ru.netology.router.Router;
import ru.netology.service.PostService;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private Router router;

    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext(JavaConfig.class);

        final var controller = context.getBean(PostController.class);
        final var service = context.getBean(PostService.class);
        final var postRepository = context.getBean(PostRepository.class);

        router = new Router(controller);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            router.handleRoute(req, resp);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        catch (NotFoundException | IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

