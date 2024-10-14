mc alias set myminio http://localhost:9000 minioadmin minioadmin


mc admin config set myminio notify_amqp:attachments_queue \
  url="amqp://guest:guest@rabbitmq:5672" \
  exchange="minio_events" \
  exchange_type="topic" \
  routing_key="attachments" \
  durable="on" \
  auto_deleted="off" \
  delivery_mode="2" \
  queue_limit="10000" \
  comment="RabbitMQ notifications for attachments"
mc admin service restart myminio

mc admin config set myminio notify_amqp:faces_queue \
  url="amqp://guest:guest@rabbitmq:5672" \
  exchange="minio_events" \
  exchange_type="topic" \
  routing_key="faces" \
  durable="on" \
  auto_deleted="off" \
  delivery_mode="2" \
  queue_limit="10000" \
  comment="RabbitMQ notifications for faces"
mc admin service restart myminio

mc admin config set myminio notify_amqp:fingerprints_queue \
  url="amqp://guest:guest@rabbitmq:5672" \
  exchange="minio_events" \
  exchange_type="topic" \
  routing_key="fingerprints" \
  durable="on" \
  auto_deleted="off" \
  delivery_mode="2" \
  queue_limit="10000" \
  comment="RabbitMQ notifications for fingerprints"
mc admin service restart myminio

mc admin config set myminio notify_amqp:videos_queue \
  url="amqp://guest:guest@rabbitmq:5672" \
  exchange="minio_events" \
  exchange_type="topic" \
  routing_key="videos" \
  durable="on" \
  auto_deleted="off" \
  delivery_mode="2" \
  queue_limit="10000" \
  comment="RabbitMQ notifications for videos"
mc admin service restart myminio

mc admin config set myminio notify_amqp:temp_queue \
  url="amqp://guest:guest@rabbitmq:5672" \
  exchange="minio_events" \
  exchange_type="topic" \
  routing_key="temp" \
  durable="on" \
  auto_deleted="off" \
  delivery_mode="2" \
  queue_limit="10000" \
  comment="RabbitMQ notifications for temporary files"
mc admin service restart myminio

  mc admin config set myminio notify_amqp:temp-delete_queue \
    url="amqp://guest:guest@rabbitmq:5672" \
    exchange="minio_events" \
    exchange_type="topic" \
    routing_key="temp-delete" \
    durable="on" \
    auto_deleted="off" \
    delivery_mode="2" \
    queue_limit="10000" \
    comment="RabbitMQ delete for temporary files"
mc admin service restart myminio

mc event remove myminio/videos --force
mc event remove myminio/fingerprints --force
mc event remove myminio/attachments --force
mc event remove myminio/faces --force
mc event remove myminio/temp --force
mc event remove myminio/temp-delete --force


mc event add myminio/videos arn:minio:sqs::videos_queue:amqp --event put
mc event add myminio/attachments arn:minio:sqs::attachments_queue:amqp --event put
mc event add myminio/fingerprints arn:minio:sqs::fingerprints_queue:amqp --event put
mc event add myminio/faces arn:minio:sqs::faces_queue:amqp --event put
mc event add myminio/temp arn:minio:sqs::temp_queue:amqp --event put
mc event add myminio/temp arn:minio:sqs::temp-delete_queue:amqp --event delete



