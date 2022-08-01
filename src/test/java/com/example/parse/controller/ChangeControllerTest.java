package com.example.parse.controller;

import com.example.parse.exception.WrongParamsException;
import com.example.parse.facade.ChangeFacade;
import com.example.parse.facade.impl.DefaultChangeFacade;
import com.example.parse.model.Setting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ChangeControllerTest {

    @Mock
    private ChangeFacade changeFacade;
    @InjectMocks
    private ChangeController changeController;

    @DisplayName("Test case return default map.")
    @Test
    void shouldReturnDefaultMap(){
        Map<String, List<String>> defaultMap = Map.of("default", List.of("default"));
        Mockito.when(changeFacade.getFutureChanges(any(), any())).thenReturn(defaultMap);
        Setting defaultSetting = new Setting();
        defaultSetting.setPath("default");
        defaultSetting.setInstructions(new ArrayList<>());
        Map<String, List<String>> actualMap = changeController.getChanges(defaultSetting);
        Assertions.assertEquals(defaultMap, actualMap);
    }

    @DisplayName("Test case throw exception.")
    @Test
    void shouldThrowException(){
        Setting defaultSetting = new Setting();
        defaultSetting.setPath("default");
        Assertions.assertThrows(WrongParamsException.class,() -> {
            changeController.getChanges(defaultSetting);
        },"Instructions can't be null");
    }
}