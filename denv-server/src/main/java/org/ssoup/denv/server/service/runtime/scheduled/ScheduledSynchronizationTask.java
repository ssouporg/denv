package org.ssoup.denv.server.service.runtime.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.server.service.runtime.sync.SynchronizationService;

/**
 * User: ALB
 * Date: 15/11/2014 18:31
 */
@Service
public class ScheduledSynchronizationTask {

    private SynchronizationService synchronizationService;

    @Autowired
    public ScheduledSynchronizationTask(SynchronizationService synchronizationService) {
        this.synchronizationService = synchronizationService;
    }

    @Scheduled(fixedDelayString = "${SCHEDULED_SYNC_DELAY}")
    public void sync() throws DenvException {
        this.synchronizationService.updateActualState();
        this.synchronizationService.moveTowardsDesiredState();
    }
}
