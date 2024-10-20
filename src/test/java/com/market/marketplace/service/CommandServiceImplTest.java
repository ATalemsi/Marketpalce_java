package com.market.marketplace.service;
import com.market.marketplace.dao.CommandDao;
import com.market.marketplace.entities.Command;
import com.market.marketplace.entities.enums.CommandStatus;
import com.market.marketplace.service.serviceImpl.CommandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class CommandServiceImplTest  {
    @Mock
    private CommandDao commandDAO;

    @InjectMocks
    private CommandServiceImpl commandService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCancelCommand_Success() {
        int commandId = 1;

        // Mocked command
        Command command = new Command();
        command.setId(commandId);
        command.setStatus(CommandStatus.PENDING);

        // Configure the mock to return the mocked command
        when(commandDAO.findById(commandId)).thenReturn(command);

        // Call the method under test
        boolean result = commandService.cancelCommand(commandId);

        // Verify the result and interactions with the mock
        assertTrue(result);
        verify(commandDAO, times(1)).findById(commandId);
        verify(commandDAO, times(1)).update(command);
        assertEquals(CommandStatus.CANCELED, command.getStatus());
    }

    @Test
    public void testCancelCommand_Fail() {
        int commandId = 1;

        // Mocked command with a status that cannot be canceled
        Command command = new Command();
        command.setId(commandId);
        command.setStatus(CommandStatus.EXPIRED);

        // Configure the mock to return the mocked command
        when(commandDAO.findById(commandId)).thenReturn(command);

        // Call the method under test
        boolean result = commandService.cancelCommand(commandId);

        // Verify the result and interactions with the mock
        assertFalse(result);
        verify(commandDAO, times(1)).findById(commandId);
        verify(commandDAO, times(0)).update(command); // Should not call update
    }
}
