package springbootapp.minivms.events;

import java.util.UUID;

public record WorkerRegisteredEvent(UUID workerUuid, UUID workOrderUuid) {

}
