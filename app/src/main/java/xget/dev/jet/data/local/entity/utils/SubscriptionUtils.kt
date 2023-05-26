package xget.dev.jet.data.local.entity.utils

import xget.dev.jet.data.local.entity.SubscriptionEntity
import xget.dev.jet.domain.model.mqtt.Subscription


fun SubscriptionEntity.toSubscription(): Subscription = Subscription(
    this.topic,
    this.qos,
    this.clientHandle,
    this.notify.toBoolean()
)

fun Subscription.toSubscriptionEntity(): SubscriptionEntity = SubscriptionEntity(
    this.clientHandle,
    this.topic,
    this.isEnableNotifications.toInt(),
    this.qos
)