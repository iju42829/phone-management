package study.phonemanagement.controller.phone;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.service.phone.PhoneService;
import study.phonemanagement.service.phone.response.PhoneResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/phones")
@RequiredArgsConstructor
public class PhoneController {

    private final PhoneService phoneService;

    @GetMapping
    public String phoneMainPage(@RequestParam(required = false) String searchWord,
                                @RequestParam(required = false) Manufacturer manufacturer,
                                @RequestParam(defaultValue = "1") Integer pageNumber,
                                @RequestParam(defaultValue = "20") Integer pageSize,
                                Model model) {
        Page<PhoneResponse> page = phoneService.getAllPhones(searchWord, manufacturer, pageNumber, pageSize);

        int blockSize  = 10;
        int current    = page.getNumber() + 1;
        int totalPages = page.getTotalPages();

        int startPage = ((current - 1) / blockSize) * blockSize + 1;
        int endPage   = Math.min(startPage + blockSize - 1, totalPages);

        List<Integer> pageNumbers =
                IntStream.rangeClosed(startPage, endPage)
                        .boxed()
                        .collect(Collectors.toList());

        model.addAttribute("phones",      page.getContent());
        model.addAttribute("page",        page);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("startPage",   startPage);
        model.addAttribute("endPage",     endPage);
        model.addAttribute("searchWord", searchWord);

        model.addAttribute("manufacturer",   manufacturer != null ? manufacturer.name() : "");
        model.addAttribute("manufacturers",  Manufacturer.values());

        return "phones/phone";
    }
}
