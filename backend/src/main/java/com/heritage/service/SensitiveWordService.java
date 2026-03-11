package com.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.dto.SensitiveWordDTO;
import com.heritage.entity.SensitiveWord;
import com.heritage.exception.BusinessException;
import com.heritage.mapper.SensitiveWordMapper;
import com.heritage.vo.SensitiveWordImportResultVO;
import com.heritage.vo.SensitiveWordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SensitiveWordService {
    private final SensitiveWordMapper sensitiveWordMapper;

    public SensitiveWordService(SensitiveWordMapper sensitiveWordMapper) {
        this.sensitiveWordMapper = sensitiveWordMapper;
    }

    public Page<SensitiveWordVO> getPage(Integer page, Integer size, String keyword, Integer status) {
        Page<SensitiveWord> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<SensitiveWord> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(SensitiveWord::getWord, keyword.trim());
        }
        if (status != null) {
            wrapper.eq(SensitiveWord::getStatus, status);
        }
        wrapper.orderByDesc(SensitiveWord::getUpdateTime).orderByDesc(SensitiveWord::getId);
        Page<SensitiveWord> entityPage = sensitiveWordMapper.selectPage(pageParam, wrapper);
        Page<SensitiveWordVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        voPage.setRecords(entityPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Transactional
    public void add(SensitiveWordDTO dto) {
        String word = normalizeWord(dto.getWord());
        validateDuplicate(word, null);
        SensitiveWord entity = new SensitiveWord();
        entity.setWord(word);
        entity.setStatus(dto.getStatus());
        entity.setRemark(trimToNull(dto.getRemark()));
        sensitiveWordMapper.insert(entity);
    }

    @Transactional
    public void update(Long id, SensitiveWordDTO dto) {
        SensitiveWord entity = requireById(id);
        String word = normalizeWord(dto.getWord());
        validateDuplicate(word, id);
        entity.setWord(word);
        entity.setStatus(dto.getStatus());
        entity.setRemark(trimToNull(dto.getRemark()));
        sensitiveWordMapper.updateById(entity);
    }

    @Transactional
    public void delete(Long id) {
        requireById(id);
        sensitiveWordMapper.deleteById(id);
    }

    @Transactional
    public SensitiveWordImportResultVO importWords(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("导入文件不能为空");
        }
        String fileName = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
        if (!(fileName.endsWith(".txt") || fileName.endsWith(".csv"))) {
            throw new BusinessException("仅支持 txt 或 csv 文件");
        }

        Set<String> words = new LinkedHashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = extractWord(line, fileName.endsWith(".csv"));
                if (word != null) {
                    words.add(word);
                }
            }
        } catch (IOException e) {
            throw new BusinessException("读取导入文件失败");
        }

        List<String> duplicateSamples = new ArrayList<>();
        int success = 0;
        for (String word : words) {
            if (existsWord(word, null)) {
                if (duplicateSamples.size() < 10) {
                    duplicateSamples.add(word);
                }
                continue;
            }
            SensitiveWord entity = new SensitiveWord();
            entity.setWord(word);
            entity.setStatus(1);
            sensitiveWordMapper.insert(entity);
            success++;
        }

        SensitiveWordImportResultVO result = new SensitiveWordImportResultVO();
        result.setTotalCount(words.size());
        result.setSuccessCount(success);
        result.setSkippedCount(words.size() - success);
        result.setDuplicateSamples(duplicateSamples);
        return result;
    }

    public boolean containsSensitiveWord(String text) {
        if (!StringUtils.hasText(text)) {
            return false;
        }
        String normalized = text.toLowerCase();
        return listActiveWords().stream().anyMatch(normalized::contains);
    }

    public String replaceSensitiveWord(String text) {
        if (!StringUtils.hasText(text)) {
            return text;
        }
        String masked = text;
        for (String word : listActiveWords()) {
            String stars = word.replaceAll(".", "*");
            masked = Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE)
                    .matcher(masked)
                    .replaceAll(stars);
        }
        return masked;
    }

    private SensitiveWord requireById(Long id) {
        SensitiveWord entity = sensitiveWordMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("敏感词不存在");
        }
        return entity;
    }

    private SensitiveWordVO toVO(SensitiveWord entity) {
        SensitiveWordVO vo = new SensitiveWordVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    private void validateDuplicate(String word, Long excludeId) {
        if (existsWord(word, excludeId)) {
            throw new BusinessException("敏感词已存在");
        }
    }

    private boolean existsWord(String word, Long excludeId) {
        LambdaQueryWrapper<SensitiveWord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SensitiveWord::getWord, word);
        if (excludeId != null) {
            wrapper.ne(SensitiveWord::getId, excludeId);
        }
        return sensitiveWordMapper.selectCount(wrapper) > 0;
    }

    private String normalizeWord(String word) {
        String normalized = trimToNull(word);
        if (!StringUtils.hasText(normalized)) {
            throw new BusinessException("敏感词不能为空");
        }
        return normalized.toLowerCase();
    }

    private String extractWord(String line, boolean csvMode) {
        if (line == null) {
            return null;
        }
        String candidate = csvMode ? line.split(",", -1)[0] : line;
        candidate = trimToNull(candidate);
        return candidate == null ? null : candidate.toLowerCase();
    }

    private String trimToNull(String text) {
        if (text == null) {
            return null;
        }
        String trimmed = text.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private List<String> listActiveWords() {
        LambdaQueryWrapper<SensitiveWord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SensitiveWord::getStatus, 1).orderByAsc(SensitiveWord::getId);
        return sensitiveWordMapper.selectList(wrapper).stream()
                .map(SensitiveWord::getWord)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
    }
}
