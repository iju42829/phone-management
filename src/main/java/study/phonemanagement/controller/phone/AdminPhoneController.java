package study.phonemanagement.controller.phone;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import study.phonemanagement.controller.phone.request.CreatePhoneRequest;
import study.phonemanagement.controller.phone.request.UpdatePhoneRequest;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Status;
import study.phonemanagement.entity.phone.Storage;
import study.phonemanagement.service.phone.PhoneService;
import study.phonemanagement.service.phone.response.CachedListPhoneResponse;
import study.phonemanagement.service.phone.response.ListPhoneResponse;
import study.phonemanagement.service.phone.response.UpdatePhoneResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
@RequestMapping("/admin/phones")
@RequiredArgsConstructor
public class AdminPhoneController {

    private final PhoneService phoneService;

    @ModelAttribute("manufacturers")
    public Manufacturer[] manufacturers() {
        return Manufacturer.values();
    }

    @ModelAttribute("storages")
    public Storage[] storages() {
        return Storage.values();
    }

    @ModelAttribute("statuses")
    public Status[] statuses() {
        return Status.values();
    }

    @GetMapping
    public String phoneMainPage(@RequestParam(required = false) String searchWord,
                                @RequestParam(required = false) Manufacturer manufacturer,
                                @RequestParam(defaultValue = "1") Integer pageNumber,
                                @RequestParam(defaultValue = "20") Integer pageSize,
                                Model model) {
        CachedListPhoneResponse cachedPage = phoneService.getAllPhones(null, searchWord, manufacturer, pageNumber, pageSize);

        Page<ListPhoneResponse> page = new PageImpl<>(
                cachedPage.getContent(), PageRequest.of(cachedPage.getPageNumber() - 1,
                cachedPage.getPageSize()),
                cachedPage.getTotalElements()
        );

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

        return "phones/adminPhone";
    }

    @GetMapping("/create")
    public String phoneCreatePage(Model model) {
        model.addAttribute("phone", new CreatePhoneRequest());

        return "phones/adminPhoneCreate";
    }

    @PostMapping
    public String addPhone(@Validated @ModelAttribute("phone") CreatePhoneRequest createPhoneRequest,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult: {}", bindingResult);

            return "phones/adminPhoneCreate";
        }

        phoneService.createPhone(createPhoneRequest);

        return "redirect:/admin/phones";
    }

    @DeleteMapping("/{phoneId}")
    public String removePhone(@PathVariable Long phoneId) {
        phoneService.deletePhone(phoneId);

        return "redirect:/admin/phones";
    }

    @GetMapping("/{phoneId}/edit")
    public String editPhonePage(Model model, @PathVariable Long phoneId) {
        UpdatePhoneResponse phone = phoneService.getPhoneForUpdate(phoneId);

        model.addAttribute("phoneId", phoneId);
        model.addAttribute("phone", phone);

        return "phones/adminPhoneEdit";
    }

    @PatchMapping("/{phoneId}")
    public String editPhone(Model model,
                            @PathVariable Long phoneId,
                            @Validated @ModelAttribute("phone") UpdatePhoneRequest updatePhoneRequest,
                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("phoneId", phoneId);
            log.info("bindingResult: {}", bindingResult);

            return "phones/adminPhoneEdit";
        }

        phoneService.update(phoneId, updatePhoneRequest);

        return "redirect:/admin/phones";
    }
}
