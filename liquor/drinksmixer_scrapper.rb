require 'rubygems'  
require 'nokogiri'  
require 'open-uri'  
require 'csv'
require 'json'

module DrinksmixerScraper

  class Scraper

    HOST_NAME   = 'http://www.drinksmixer.com/'
    PAGE        = 'drink#.html'
    REPLACE_KEY = '#'
    START_ENTRY = 1
    END_ENTRY   = 12315
    FORMAT      = 'csv' # csv or json

    def scrape
      (START_ENTRY..END_ENTRY).each do |i|
        url = "#{HOST_NAME}#{PAGE.gsub(REPLACE_KEY, i.to_s)}"
        doc = Nokogiri::HTML(open(url))  

        if doc.xpath('//body').size() > 0
          # Breadcrumb trail, ex: Cocktails > Long drinks > Vodka Based
          breadcrumb = doc.xpath(".//div[@class='pm']").inner_html
          breadcrumb.gsub!(/<a.+?>|<\/a>/, '').gsub!('&gt;','>')

          # Drink Details
          drink_name = doc.xpath(".//h1[@class='fn recipe_title']").inner_text
          description = doc.xpath(".//div[@class='summary RecipeDirections']").inner_html
          instructions = doc.xpath(".//div[@class='RecipeDirections instructions']").inner_html

          # Ingredients
          ingredients = []
          doc.xpath(".//span[@class='ingredient']").each do |node|
            amount = node.xpath(".//span[@class='amount']").inner_text
            name = node.xpath(".//span[@class='name']").inner_html.gsub(/<a.+?>|<\/a>/, '')
            ingredients << { :amount => amount, :name => name }
          end

          # Drink Ratings
          rating_node = doc.xpath(".//div[@class='ratingsBox rating']")
          rating = rating_node.xpath('./div/div[1]').inner_text
          avg_rating = rating_node.xpath("./div/div[@class='average']").inner_text
          votes = rating_node.xpath("./div/span[@class='count']").inner_text

          # Served In / Alcohol Content
          serve_in_node = doc.xpath(".//div[@class='recipeStats']")
          glass = serve_in_node.xpath('.//img').attribute('title')
          alcohol_content = serve_in_node.xpath('.//b').inner_text

          hsh = { :name => drink_name, :breadcrumb => breadcrumb, :description => description,
                  :instructions => instructions, :ingredients => ingredients, 
                  :rating => { :number => rating, :average => avg_rating, :votes => :votes },
                  :glass => :glass, :alcohol_content => alcohol_content }

          # Write to CSV or JSON
          file_name = "drink_#{i}_#{drink_name.downcase.gsub(' ', '_')}"
          puts file_name

          if FORMAT.eql? 'csv'
            write_to_csv(file_name, hsh)
          elsif FORMAT.eql? 'json'
            write_to_json(file_name, hsh)
          end
        end
      end
    end

    private 

    def write_to_csv(file_name, data)
      return false unless file_name

      # Write to CSV
      CSV.open("#{file_name}.csv", 'wb') do |csv|
        csv << ['name', 'breadcrumb', 'description', 'instructions', 'ingredients', 
                'rating', 'avg_rating', 'votes', 'glass', 'alcohol_content']

        ingredients_ary = []
        data[:ingredients].each do |ingredient|
          ingredients_ary << "#{ingredient[:amount]} #{ingredient[:name]}"
        end

        csv << [ data[:name], data[:breadcrumb], data[:description], data[:instructions], 
                 ingredients_ary.join(','), data[:rating][:number], data[:rating][:average],
                 data[:rating][:votes], data[:glass], data[:alcohol_content] ]
      end
    end


    def write_to_json(file_name, data)
      return false unless file_name

      f = File.open("#{file_name}.json", 'wb')
      f.write(data.to_json)
      f.close
    end

  end
  
  # Run
  def self.scrape
    s = Scraper.new
    s.scrape
  end

end

if __FILE__ == $0
  DrinksmixerScraper.scrape
end
