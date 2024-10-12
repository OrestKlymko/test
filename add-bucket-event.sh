mc alias set myminio http://localhost:9000 minioadmin minioadmin


mc admin config set myminio notify_amqp:attachments_queue \
  url="amqp://guest:guest@rabbitmq:5672" \
  exchange="minio_events" \
  exchange_type="topic" \
  routing_key="minio.attachments" \
  durable="on" \
  auto_deleted="off" \
  delivery_mode="2" \
  queue_limit="10000" \
  comment="RabbitMQ notifications for attachments"

mc admin config set myminio notify_amqp:faces_queue \
  url="amqp://guest:guest@rabbitmq:5672" \
  exchange="minio_events" \
  exchange_type="topic" \
  routing_key="minio.faces" \
  durable="on" \
  auto_deleted="off" \
  delivery_mode="2" \
  queue_limit="10000" \
  comment="RabbitMQ notifications for faces"

mc admin config set myminio notify_amqp:fingerprints_queue \
  url="amqp://guest:guest@rabbitmq:5672" \
  exchange="minio_events" \
  exchange_type="topic" \
  routing_key="minio.fingerprints" \
  durable="on" \
  auto_deleted="off" \
  delivery_mode="2" \
  queue_limit="10000" \
  comment="RabbitMQ notifications for fingerprints"

mc admin config set myminio notify_amqp:videos_queue \
  url="amqp://guest:guest@rabbitmq:5672" \
  exchange="minio_events" \
  exchange_type="topic" \
  routing_key="minio.videos" \
  durable="on" \
  auto_deleted="off" \
  delivery_mode="2" \
  queue_limit="10000" \
  comment="RabbitMQ notifications for videos"

mc event add myminio/attachments arn:minio:sqs::primary:amqp --event put --suffix .jpg --suffix .png --queue=attachments --routing_key minio.attachments
mc event add myminio/faces arn:minio:sqs::primary:amqp --event put --queue=faces --routing_key minio.faces
mc event add myminio/fingerprints arn:minio:sqs::primary:amqp --event put --queue=fingerprints --routing_key minio.fingerprints
mc event add myminio/videos arn:minio:sqs::primary:amqp --event put --queue=videos --routing_key minio.videos
mc event add myminio/temp arn:minio:sqs::amqp --event delete --suffix .*


mc admin service restart myminio