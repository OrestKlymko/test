mc alias set myminio http://localhost:9000 minioadmin minioadmin

mc admin config set myminio notify_amqp:primary \
  url="amqp://guest:guest@rabbitmq:5672" \
  exchange="minio_events" \
  exchange_type="attachments" \
  routing_key="minio.*" \
  durable="on" \
  auto_deleted="off" \
  delivery_mode="2" \
  queue_limit="10000" \
  comment="RabbitMQ notifications"

mc admin service restart myminio

mc event add myminio/attachments arn:minio:sqs::primary:amqp --event put

mc event list myminio/attachments
