package com.zerobase.leisure.controller.leisure;

import com.zerobase.leisure.domain.dto.leisure.LeisureBlackListDto;
import com.zerobase.leisure.domain.form.AddLeisureBlackListForm;
import com.zerobase.leisure.domain.model.WebResponseData;
import com.zerobase.leisure.service.leisure.LeisureBlackListService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LeisureBlackListController {
    private final LeisureBlackListService leisureBlackListService;

    //블랙리스트 등록
    @PostMapping("/seller/leisure/blacklist")
    public @ResponseBody WebResponseData<String> addLeisureBlackList(@RequestBody AddLeisureBlackListForm form) {
        leisureBlackListService.addLeisureBlackList(form);
        return WebResponseData.ok("블랙리스트에 추가했습니다.");
    }

    //블랙 리스트 삭제
    @DeleteMapping("/seller/leisure/blacklist")
    public @ResponseBody WebResponseData<String> deleteLeisureBlackList(@RequestParam Long leisureBlackListId) {
        leisureBlackListService.deleteLeisureBlackList(leisureBlackListId);
        return WebResponseData.ok("성공적으로 삭제 되었습니다.");
    }

    @GetMapping("/seller/leisure/blacklist")
    public @ResponseBody WebResponseData<Page<LeisureBlackListDto>> getLeisureBlackList(@RequestParam Long leisureId,
                                                                                        final Pageable pageable) {
        Page<LeisureBlackListDto> leisureBlackListDtos = leisureBlackListService.getLeisureBlackList(leisureId, pageable);
        return WebResponseData.ok(leisureBlackListDtos);
    }

}
