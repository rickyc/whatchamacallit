require 'rubygems'  
require 'nokogiri'  
require 'open-uri'  
require 'csv'
require 'json'


url = 'http://nycopendata.socrata.com/browse?&page=1'

doc = Nokogiri::HTML(open(url))  
last_page = doc.xpath(".//a[@class='end lastLink button']").attribute('href').to_s.split('=').last

data = []

(1..last_page.to_i).each do |i|

  url = "http://nycopendata.socrata.com/browse?&page=#{i}"
  doc = Nokogiri::HTML(open(url))  
  puts url

  doc.xpath('.//tbody/tr').each do |tr|
    row = tr.xpath(".//td[@class='nameDesc']")
    name = row.xpath('.//a').inner_text
    category = row.xpath(".//span[@class='category infoItem']").inner_text
    tags = row.xpath(".//span[@class='tags infoItem']").inner_text
    desc = row.xpath(".//div[@class='description']").inner_text

    data << [name, category, tags, desc]

  end

end

CSV.open('list.csv', 'wb') do |csv|
  csv << ['name', 'category', 'tags', 'desc']
  data.each { |d| csv << d }
end
