package com.todomicroservice.service;

import com.todomicroservice.dto.request.ToDoListCreateRequestDto;
import com.todomicroservice.entity.ToDoList;
import com.todomicroservice.repository.ToDoListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.nio.file.AccessDeniedException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToDoListServiceTest {

    @Mock
    private ToDoListRepository toDoListRepository;

    @Mock
    private ToDoListConverterService toDoListConverterService;

    @InjectMocks
    private ToDoListService toDoListService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createList_ShouldAddUsernameAndSave() {
        ToDoListCreateRequestDto dto = new ToDoListCreateRequestDto();
        ToDoList toDoList = new ToDoList();
        toDoList.setMembers(new HashSet<>());

        when(toDoListConverterService.convertToToDoList(dto)).thenReturn(toDoList);
        when(toDoListRepository.save(any(ToDoList.class))).thenAnswer(i -> i.getArgument(0));

        ToDoList result = toDoListService.createList("username", dto);

        assertTrue(result.getMembers().contains("username"));
        verify(toDoListRepository, times(1)).save(toDoList);
    }

    @Test
    void getMyLists_ShouldReturnLists() {
        List<String> mockTitles = List.of("List1", "List2");

        when(toDoListRepository.findTitlesByMember("username"))
                .thenReturn(mockTitles);

        List<String> result = toDoListService.getMyListsTitle("username");

        assertEquals(2, result.size());
        assertEquals("List1", result.get(0));
        verify(toDoListRepository).findTitlesByMember("username");
    }

    @Test
    void addMember_ShouldAddIfAuthorized() throws Exception {
        ToDoList list = new ToDoList();
        list.setId(1L);
        list.setMembers(new HashSet<>(Set.of("member")));

        when(toDoListRepository.findById(1L)).thenReturn(Optional.of(list));

        toDoListService.addMember(1L, "username", "member");

        assertTrue(list.getMembers().contains("username"));
        verify(toDoListRepository).save(list);
    }

    @Test
    void addMember_ShouldThrowAccessDeniedIfNotAuthorized() {
        ToDoList list = new ToDoList();
        list.setMembers(new HashSet<>(Set.of("member")));

        when(toDoListRepository.findById(1L)).thenReturn(Optional.of(list));

        assertThrows(AccessDeniedException.class,
                () -> toDoListService.addMember(1L, "username", "member1"));
    }

    @Test
    void getAuthorizedListById_ShouldReturnListIfAuthorized() {
        ToDoList list = new ToDoList();
        list.setId(1L);
        list.setMembers(new HashSet<>(Set.of("member")));

        when(toDoListRepository.findById(1L)).thenReturn(Optional.of(list));

        ToDoList result = toDoListService.getAuthorizedListById(1L, "member");

        assertNotNull(result);
        assertEquals(list, result);
    }

    @Test
    void getAuthorizedListById_ShouldThrowIfNotAuthorized() {
        ToDoList list = new ToDoList();
        list.setMembers(new HashSet<>(Set.of("member")));

        when(toDoListRepository.findById(1L)).thenReturn(Optional.of(list));

        assertThrows(RuntimeException.class,
                () -> toDoListService.getAuthorizedListById(1L, "notmember"));
    }
}
