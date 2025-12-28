package holymagic.typeservice;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class TypeServiceApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(TypeServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

}
