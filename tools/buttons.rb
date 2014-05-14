require_relative 'drawables_common'

DEFAULT_DENSITY_MAP = DensityMap.default(90)

LAYOUTS = {
  default: ImageLayout.with_layers('Default'),
  disabled: ImageLayout.with_layers('Disabled')
}

def create_button_drawables(svg_path, resource_dir)
  DEFAULT_DENSITY_MAP.each_density(resource_dir) do |dest_dir, dpi|
    export_button_drawables svg_path, dest_dir, dpi
  end
end

def export_button_drawables(svg_path, dest_dir, dpi)
  LAYOUTS.each_pair do |name, layout|
    Inkscape.export_png svg_path, png_path_from(svg_path, dest_dir, name), dpi, layout     
  end
end

def png_path_from(svg_path, dest_dir, suffix)
  base_name = svg_path.basename(".*")
  dest_png = dest_dir + "#{base_name}_#{suffix}.png"
end

def create_main_drawables(svg_path, resource_dir)
  DEFAULT_DENSITY_MAP.each_density(resource_dir) do |dest_dir, dpi|
    export_main_drawables svg_path, dest_dir, dpi
  end
end

def export_main_drawables(svg_path, dest_dir, dpi)
  Inkscape.export_png svg_path, main_png_path_from(svg_path, dest_dir), dpi, ImageLayout.with_layers('Default')    
end

def main_png_path_from(svg_path, dest_dir)
  base_name = svg_path.basename(".*")
  dest_png = dest_dir + "#{base_name}.png"
end
