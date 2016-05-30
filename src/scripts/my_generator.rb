require 'apache-loggen/base'
require 'digest/sha1'
require 'csv'

class MyGenerator < LogGenerator::Apache

  def initialize
    super
    @arr = []
    CSV.foreach("#{File.dirname(__FILE__)}/../../src/main/resources/userdata.csv") do | row |
      @arr << row[9]
    end
  end

  def format(record, config)
    record['user'] = @arr[grand(@arr.length)]
    return %[#{record['host']} - #{record['user']} [#{Time.now.strftime('%d/%b/%Y:%H:%M:%S %z')}] "#{record['method']} #{record['path']} HTTP/1.1" #{record['code']} #{record['size']} "#{record['referer']}" "#{record['agent']}"\n]
  end
end

LogGenerator.generate(nil, MyGenerator.new)
