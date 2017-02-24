package com.livefyre.akka.cdi

import javax.inject.Singleton

import com.livefyre.tools.service.BaseService

@Singleton
class CountingService extends BaseService {
  def increment(count: Int) = count + 1
}
